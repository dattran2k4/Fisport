package com.fisport.service.impl;

import com.fisport.common.ESubFieldStatus;
import com.fisport.dto.request.SubFieldRequest;
import com.fisport.dto.response.SubFieldResponse;
import com.fisport.exception.InvalidDataException;
import com.fisport.exception.ResourceNotFoundException;
import com.fisport.model.Field;
import com.fisport.model.SubField;
import com.fisport.repository.FieldRepository;
import com.fisport.repository.SubFieldRepository;
import com.fisport.service.SubFieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubFieldServiceImpl implements SubFieldService {
    private final FieldRepository fieldRepository;
    private final SubFieldRepository subFieldRepository;

    @Override
    public List<SubFieldResponse> getAllSubFields(Long fieldId, ESubFieldStatus status) {
        List<SubField> list = subFieldRepository.findAllSubFields(fieldId, status);
        return list.stream().map(this::toDto).toList();
    }

    @Override
    public SubFieldResponse getSubFieldById(Long id) {
        SubField s = getSubField(id);
        return toDto(s);
    }

    @Override
    public List<SubFieldResponse> getSubFieldsByFieldId(Long fieldId) {
        List<SubField> s = subFieldRepository.findAllByFieldId(fieldId);
        return s.stream().map(this::toDto).toList();
    }

    @Override
    public void createSubField(SubFieldRequest request, String username) throws AccessDeniedException {
        Field field = fieldRepository.findById(request.getFieldId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sân"));

        if (!field.getOwner().getUsername().equals(username)) {
            throw new AccessDeniedException("Không có quyền truy cập sân này");
        }

        if (subFieldRepository.existsByNameAndFieldId(request.getName(), request.getFieldId())) {
            throw new InvalidDataException("Tên sân con đã tồn tại trong sân");
        }

        SubField subField = SubField.builder()
                .name(request.getName())
                .status(request.getStatus())
                .field(field)
                .build();

        subFieldRepository.save(subField);
    }

    //Không update field_id
    @Override
    public void updateSubField(Long id, SubFieldRequest request, String username) throws AccessDeniedException {
        SubField subField = getSubField(id);

        if (subFieldRepository.existsByNameAndFieldId(request.getName(), request.getFieldId())) {
            throw new InvalidDataException("Tên sân con đã tồn tại trong sân");
        }

//        Field field = fieldRepository.findById(request.getFieldId())
//                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sân"));
//
//        if (!field.getOwner().getUsername().equals(username)) {
//            throw new AccessDeniedException("Không có quyền truy cập sân này");
//        }

        subField.setName(request.getName());
        subField.setStatus(request.getStatus());

        subFieldRepository.save(subField);
    }

    @Override
    public void deleteSubField(Long id, String username) throws AccessDeniedException {
        SubField subField = getSubField(id);
        if (!subField.getField().getOwner().getUsername().equals(username)) {
            throw new AccessDeniedException("Bạn không có quyền xóa SubField này");
        }
        subFieldRepository.delete(subField);
    }

    private SubField getSubField(Long id) {
        return subFieldRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sân"));
    }

    private SubFieldResponse toDto(SubField subField) {
        return SubFieldResponse.builder()
                .id(subField.getId())
                .name(subField.getName())
                .status(subField.getStatus())
                .fieldId(subField.getField().getId())
                .created_at(subField.getCreate_at())
                .update_at(subField.getUpdate_at())
                .build();
    }
}
