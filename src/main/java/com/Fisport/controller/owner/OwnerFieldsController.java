package com.Fisport.controller.owner;

import com.Fisport.dto.request.FieldCreateRequest;
import com.Fisport.dto.request.ServiceItemsRequest;
import com.Fisport.service.*;
import com.Fisport.service.impl.SessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/owner/fields")
public class OwnerFieldsController {

    @Autowired
    private CityService cityService;

    @Autowired
    private FeatureService featureService;

    @Autowired
    private FieldTypeService fieldTypeService;

    @Autowired
    private FieldService fieldService;

    @Autowired
    private ServiceItemService serviceItemService;

    @GetMapping
    public String listFields(Model model, HttpServletRequest request, Principal principal) {
        model.addAttribute("content", "fields-list");
        model.addAttribute("currentUri", request.getRequestURI());
        model.addAttribute("fields", fieldService.getMyOwnerFields(principal.getName()));
        return "owner/fields/list";
    }

    @GetMapping("/create")
    public String createFieldForm(Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        model.addAttribute("content", "fields-create");
        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("features", featureService.getListFeatures());
        model.addAttribute("fieldTypes", fieldTypeService.findAll());
        model.addAttribute("serviceItems", serviceItemService.findAll());
        return "owner/fields/create";
    }

    @PostMapping("/save")
    public String saveField(
            @ModelAttribute FieldCreateRequest request,
            @RequestParam("image") List<MultipartFile> image,
            Model model,
            Principal principal,
            @RequestParam("serviceItemsJson") String serviceItemsJson
    ) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        List<ServiceItemsRequest> serviceItems =
                Arrays.asList(mapper.readValue(serviceItemsJson, ServiceItemsRequest[].class));
        request.setServiceItems(serviceItems);

        String uploadDir = new File("src/main/resources/static/web/img/field/FieldType"+request.getField_type()).getAbsolutePath();
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }
        for (MultipartFile file : image) {
            if (!file.isEmpty()) {
                String originalFilename = file.getOriginalFilename();
                String extension = "";
                int dotIndex = originalFilename.lastIndexOf('.');
                if (dotIndex > 0) {
                    extension = originalFilename.substring(dotIndex);
                }
                String uniqueName = UUID.randomUUID().toString().substring(0, 8) + extension;
                File destinationFile = new File(uploadDir + File.separator + uniqueName);
                file.transferTo(destinationFile);
                String imageUrl = "/web/img/field/FieldType"+request.getField_type() +"/"+ uniqueName;
                request.setBanner(imageUrl);
            }
        }
//        TODO : THÊM SERVICE VÀO BẢNG SERVICE_ITEM_FIELD , ĐÃ CÓ TOÀN BỘ THÔNG TIN SERVICE_ITEM_ID, FIELD_IT , QUANTITY , NAME
//        TODO : BỎ SELECTED TRONG SERVICE ITEM REQUEST DTO
//        TODO : THÊM SUB _FIELD VÀO BẢNG SUB_FIELD, ĐÃ CÓ NAME CỦA SUB_FIELD
        fieldService.createFieldByOwner(request,principal.getName());

        return "redirect:/owner/fields";
    }
}
