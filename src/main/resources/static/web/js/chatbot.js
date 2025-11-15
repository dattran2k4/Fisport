$(document).ready(function () {
    // Lấy các phần tử DOM (jQuery)
    const $chatPopup = $('#chatbot-popup');
    const $toggleBtn = $('#chatbot-toggle-btn');
    const $closeBtn = $('#chatbot-close-btn');

    const $chatLog = $('#chatbot-log');
    const $chatForm = $('#chatbot-form');
    const $userInput = $('#chatbot-input');
    const $sendBtn = $('#chatbot-send-btn');

    // Lấy các phần tử Modal
    const $modalOverlay = $('#booking-modal-overlay');
    const $bookingForm = $('#chatbot-booking-form');
    const $priceDisplay = $('#chatbot-modal-price');
    const $cancelBookingBtn = $('#chatbot-cancel-booking-btn');

    // *** THAY ĐỔI: Lấy URL từ data-attributes ***
    const chatApiUrl = $chatPopup.data('api-chat-stream');
    const bookingsApiUrl = $chatPopup.data('api-bookings');

    const $voucherCodeInput = $('#chatbot-modal-voucher-code');
    const $voucherIdInput = $('#chatbot-modal-voucher-id');
    const $voucherListContainer = $('#chatbot-voucher-list');
    const $voucherDiscountInput = $('#chatbot-modal-voucher-discount');

    let eventSource = null;
    let modalDataStore = null;
    let isStreamFinished = false;

    // Logic Ẩn/Hiện Cửa Sổ Chat
    $toggleBtn.on('click', function () { $chatPopup.toggleClass('active'); });
    $closeBtn.on('click', function () { $chatPopup.removeClass('active'); });

    // Logic Gửi Tin Nhắn
    $chatForm.on('submit', function (e) {
        e.preventDefault();
        const message = $userInput.val().trim();
        if (message === '') return;
        addMessageToLog('user', message);
        $userInput.val('');
        $sendBtn.prop('disabled', true);

        const $botMessageP = $('<p>');
        $chatLog.append($('<div>').addClass('message bot').append($botMessageP));

        if (eventSource) eventSource.close();
        isStreamFinished = false;

        const encodedMessage = encodeURIComponent(message);

        // *** THAY ĐỔI: Sử dụng biến URL ***
        const url = `${chatApiUrl}?message=${encodedMessage}`;

        eventSource = new EventSource(url);

        eventSource.onmessage = function (event) {
            try {
                const response = JSON.parse(event.data);
                if (response.type === 'ACTION' || response.type === 'ERROR') {
                    isStreamFinished = true;
                }
                handleStreamResponse(response, $botMessageP);
            } catch (err) {
                console.error("Lỗi parse JSON:", event.data, err);
                addMessageToLog('error', "Có lỗi khi xử lý phản hồi từ AI.");
                isStreamFinished = true;
                if (eventSource) eventSource.close();
            }
        };

        eventSource.onerror = function (err) {
            console.error("Lỗi EventSource:", err);
            if (!isStreamFinished) {
                // Đây là một lỗi bất ngờ (ví dụ: mất mạng, máy chủ sập)
                addMessageToLog('error', "Mất kết nối với máy chủ AI.");
            }
            $sendBtn.prop('disabled', false);
            if (eventSource) eventSource.close();
        };
    });

    $cancelBookingBtn.on('click', function () {
        $modalOverlay.hide();
        modalDataStore = null;
        addMessageToLog('bot', "Đã hủy đặt sân. Bạn cần em giúp gì nữa không?");
    });

    function calculateEndTime(startTime, durationMinutes) {
        // Tách giờ và phút
        const [hours, minutes] = startTime.split(':').map(Number);

        // Tạo đối tượng Date (chỉ dùng để tính toán)
        const date = new Date();
        date.setHours(hours, minutes, 0, 0); // Đặt giờ bắt đầu

        // Thêm số phút thời lượng
        date.setMinutes(date.getMinutes() + durationMinutes);

        // Lấy giờ và phút kết thúc
        const endHours = String(date.getHours()).padStart(2, '0');
        const endMinutes = String(date.getMinutes()).padStart(2, '0');

        return `${endHours}:${endMinutes}`;
    }

    function updatePrice() {
        const fieldId = $('#chatbot-modal-field-id').val();
        console.log(fieldId);
        const durationVal = $('#chatbot-modal-duration').val();
        const durationMinutes = parseInt(durationVal, 10);
        const startTime = $('#chatbot-modal-time').val();
        const discountPercent = parseInt($voucherDiscountInput.val() || '0', 10);

        if (!fieldId || !durationMinutes || !startTime) {
            $priceDisplay.html('--').removeClass('loading');
            return;
        }

        const endTime = calculateEndTime(startTime, parseInt(durationMinutes, 10));
        console.log(endTime)

        $priceDisplay.html('Đang tính...').addClass('loading');

        $.ajax({
            url: '/api/v1/bookings/preview-timingPrice',
            type: 'GET',
            data: {
                fieldId: fieldId,
                startTime: startTime,
                endTime: endTime
            },
            success: function (apiResponse) {
                if (apiResponse.status === 200 && apiResponse.data !== null) {


                    const basePrice = parseFloat(apiResponse.data);

                    let finalPrice = basePrice;
                    if (discountPercent > 0) {
                        finalPrice = basePrice * (1 - (discountPercent / 100));
                    }

                    const formattedPrice = finalPrice.toLocaleString('vi-VN', {
                        style: 'currency',
                        currency: 'VND'
                    });
                    $priceDisplay.html(formattedPrice);

                    if (discountPercent > 0) {
                        const formattedBasePrice = basePrice.toLocaleString('vi-VN', {
                            style: 'currency',
                            currency: 'VND'
                        });
                        $priceDisplay.append(` <s class="price-original">${formattedBasePrice}</s>`);
                    }
                } else {
                    $priceDisplay.html('Lỗi');
                }
            },
            error: function (xhr, status, error) {
                // Tương đương với 'catch' block
                console.error('Lỗi cập nhật giá:', error);
                $priceDisplay.html('Không thể tính');
            },
            complete: function () {
                // Tương đương với 'finally' block
                $priceDisplay.removeClass('loading');
            }
        });
    }

    $voucherListContainer.on('click', '.voucher-btn', function () {
        const $this = $(this);

        const voucherCode = $this.data('code');
        const voucherId = $this.data('id');

        $voucherListContainer.find('.voucher-btn').removeClass('active');
        $this.addClass('active');

        $voucherCodeInput.val($this.data('code'));
        $voucherIdInput.val($this.data('id'));
        $voucherDiscountInput.val($this.data('discount'));

        updatePrice();
    });



    $bookingForm.on('submit', function (e) {
        e.preventDefault();

        // ... (Logic tạo bookingRequest giữ nguyên) ...
        const date = $('#chatbot-modal-date').val();
        const startTime = $('#chatbot-modal-time').val();
        const subFieldIdVal = $('#chatbot-modal-subfield').val();
        const durationVal = $('#chatbot-modal-duration').val();
        const voucherIdVal = $('#chatbot-modal-voucher-id').val();

        const durationMinutes = parseInt(durationVal, 10);
        const endTime = calculateEndTime(startTime, durationMinutes);

        if (!subFieldIdVal || !durationVal) {
            addMessageToLog('error', 'Lỗi: Vui lòng chọn sân con và thời lượng.');
            return; // Dừng thực thi
        }

        const bookingRequest = {
            date: date,                           // "yyyy-MM-dd" (String)
            subFieldId: parseInt(subFieldIdVal, 10), // Chuyển sang số (Long)
            startTime: startTime,
            endTime: endTime,                 // "HH:mm" (String)
            duration: durationMinutes,     // Chuyển sang số (Integer)
            voucherId: voucherIdVal
        };

        addMessageToLog('bot', "Đang xử lý đặt sân, vui lòng đợi...");

        // *** THAY ĐỔI: Sử dụng biến URL ***
        $.ajax({
            type: 'POST',
            url: bookingsApiUrl, // <-- API BookingController
            contentType: 'application/json',
            data: JSON.stringify(bookingRequest),
            success: function (response) {
                if (response && response.status === 201) {
                    addMessageToLog('bot', "Đặt sân thành công! Cảm ơn bạn.");
                    $modalOverlay.hide();
                    modalDataStore = null;

                    if (response.data) {
                        const token = response.data;
                        const paymentUrl = '/thanh-toan?token=' + token;

                        addMessageToLog('bot', "Đặt sân thành công! Đang chuyển bạn đến trang thanh toán sau 2s...");

                        setTimeout(function () {
                            window.location.href = paymentUrl;
                        }, 2000);

                    } else {
                        addMessageToLog('error', 'Lỗi: Đặt sân thành công nhưng không nhận được token thanh toán.');
                    }
                } else {
                    let errorMsg = "Đặt sân thất bại.";
                    if (response && response.message) {
                        errorMsg += " " + response.message;
                    }
                    addMessageToLog('error', errorMsg);
                }
            },
        });
    });

    // --- (Sao chép các hàm helper còn lại vào đây) ---

    function handleStreamResponse(response, $botMessageP) {
        // (Code y hệt ví dụ trước)
        switch (response.type) {
            case 'TEXT':
                const currentText = $botMessageP.text();
                $botMessageP.text(currentText + response.textChunk);
                $chatLog.scrollTop($chatLog[0].scrollHeight);
                break;
            case 'ACTION':
                console.log("Action received:", response.action);
                handleAction(response.action);
                $sendBtn.prop('disabled', false);
                if (eventSource) eventSource.close();
                break;
            case 'ERROR':
                addMessageToLog('error', response.textChunk);
                $sendBtn.prop('disabled', false);
                if (eventSource) eventSource.close();
                break;
        }
    }

    $('#chatbot-modal-subfield').on('change', function () {
        const $selectedOption = $(this).find('option:selected');

        const selectedSubfieldId = $selectedOption.val();
        const associatedFieldId = $selectedOption.data('field-id');
        $('#chatbot-modal-field-id').val(associatedFieldId);
        console.log(associatedFieldId);
        const date = $('#chatbot-modal-date').val();
        const time = $('#chatbot-modal-time').val();

        $('#chatbot-modal-price').html('--');

        if (selectedSubfieldId) {
            fetchAndPopulateDurations(selectedSubfieldId, date, time);
            updatePrice();
        } else {
            // Xử lý nếu không có ID (ví dụ: chọn "Không có sân con")
            $('#chatbot-modal-duration').empty().prop('disabled', true);
        }
    });

    $('#chatbot-modal-duration').on('change', function () {
        updatePrice();
    });



    /**
 * Gọi API để lấy thời lượng khả dụng và cập nhật dropdown
 * @param {string} subfieldId - ID của sân con
 * @param {string} date - Ngày (ví dụ: '2025-11-10')
 * @param {string} time - Giờ (ví dụ: '14:00')
 */
    function fetchAndPopulateDurations(subfieldId, date, time) {
        const $durationSelect = $('#chatbot-modal-duration');
        $durationSelect.empty().prop('disabled', true); // Xóa option cũ và vô hiệu hóa

        // Giả sử API của bạn có thể truy cập qua đường dẫn này
        // THAY ĐỔI /api/booking/ NẾU CẦN
        const apiUrl = `/api/v1/sub-fields/${subfieldId}/available-durations?date=${date}&startTime=${time}`;

        $.ajax({
            url: apiUrl,
            type: 'GET',
            success: function (response) {
                $durationSelect.empty(); // Xóa placeholder "Đang tải..."

                // Dựa vào cấu trúc ApiResponse của bạn, data nằm trong response.data
                if (response && response.data && response.data.length > 0) {
                    response.data.forEach(duration => {
                        const optionText = `${duration} phút`;
                        $durationSelect.append($('<option>').val(duration).text(optionText));
                    });
                    $durationSelect.prop('disabled', false); // Kích hoạt lại
                } else {
                    // Không có thời lượng nào khả dụng
                    $durationSelect.append($('<option>').text('Giờ chơi hiện tại đang bị chiếm, bạn hãy chọn sân khác').val(''));
                }
            },
            error: function (err) {
                console.error('Lỗi khi lấy thời lượng:', err);
                $durationSelect.empty();
                $durationSelect.append($('<option>').text('Lỗi tải dữ liệu').val(''));
            }
        });
    }

    function fetchAndPopulateVouchers() {
        $.ajax({
            url: '/api/v1/vouchers/list',
            type: 'GET',
            success: function (response) {
                $voucherListContainer.empty();
                if (response.status === 200 && response.data && response.data.length > 0) {
                    response.data.forEach(voucher => {
                        // Tạo button cho mỗi voucher
                        const voucherBtn = $(`
                        <button type="button" class="voucher-btn" 
                            data-code="${voucher.code}" 
                            data-id="${voucher.id}"
                            data-discount="${voucher.discount}">
                        <strong>${voucher.code}</strong>
                        <span>${voucher.description} (Giảm ${voucher.discount}%)</span>
                    </button>
                    `);
                        $voucherListContainer.append(voucherBtn);
                    });
                } else {
                    $voucherListContainer.html('<span>Không có voucher nào.</span>');
                }
            },
            error: function () {
                $voucherListContainer.html('<span style="color: red;">Lỗi tải voucher.</span>');
            }
        });
    }

    function handleAction(action) {
        if (action.actionName === 'OPEN_BOOKING_MODAL') {

            // Truy cập vào đối tượng data bị lồng bên trong
            const nestedData = action.data.data;

            // Gán modalDataStore và lấy criteria/fields từ nestedData
            modalDataStore = nestedData;
            //Criteria JSON
            const criteria = nestedData.criteria;
            //Fields Json
            const fields = nestedData.foundFields;

            $('#chatbot-modal-date').val(criteria.date);
            $('#chatbot-modal-time').val(criteria.time);
            $('#chatbot-modal-field-id').val(fields.fieldId);

            const $subfieldSelect = $('#chatbot-modal-subfield');
            const $durationSelect = $('#chatbot-modal-duration');

            $subfieldSelect.empty();
            $durationSelect.empty().prop('disabled', true);

            if (fields && fields.length > 0) {
                fields.forEach(field => {
                    const optionText = `${field.fieldName} - ${field.name}`;
                    $subfieldSelect.append($('<option>').val(field.id)
                        .text(optionText)
                        .data('field-id', field.fieldId));
                });

                // Tự động gọi API cho sân con đầu tiên trong danh sách
                const initialSubfieldId = fields[0].id;
                const initialFieldId = fields[0].fieldId;
                $('#chatbot-modal-field-id').val(initialFieldId);
                $('#chatbot-modal-price').html('--');
                $voucherCodeInput.val('');
                $voucherIdInput.val('');
                $voucherDiscountInput.val('0');

                fetchAndPopulateVouchers();
                fetchAndPopulateDurations(initialSubfieldId, criteria.date, criteria.time);

            } else {
                // Không tìm thấy sân nào
                $subfieldSelect.append($('<option>').text('Không có sân').val(''));
                $subfieldSelect.prop('disabled', true);
                $durationSelect.append($('<option>').text('Không có sân').val(''));
            }

            $modalOverlay.css('display', 'flex');
        }
    }



    function addMessageToLog(sender, message) {
        // (Code y hệt ví dụ trước)
        const $messageDiv = $('<div>').addClass('message').addClass(sender);
        $messageDiv.append($('<p>').text(message));
        $chatLog.append($messageDiv);
        $chatLog.scrollTop($chatLog[0].scrollHeight);
    }
});