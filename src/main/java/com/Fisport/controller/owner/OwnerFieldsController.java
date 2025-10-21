package com.Fisport.controller.owner;

import com.Fisport.dto.request.FieldCreateRequest;
import com.Fisport.dto.request.ServiceItemsRequest;
import com.Fisport.dto.response.FieldHasFeatureResponse;
import com.Fisport.dto.response.FieldResponse;
import com.Fisport.dto.response.FieldServiceItemResponse;
import com.Fisport.exception.ResourceNotFoundException;
import com.Fisport.model.Field;
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
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private WardService wardService;

    @Autowired
    private SubFieldService subFieldService;

    @Autowired
    private FieldServiceItemService fieldServiceItemService;

    @Autowired
    private FieldHasFeatureService fieldHasFeatureService;
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
        fieldService.createFieldByOwner(request,principal.getName());

        return "redirect:/owner/fields";
    }

    @GetMapping("/{id}/edit")
    public String editField(@PathVariable Long id, Model model, Principal principal) {
        FieldResponse field = fieldService.getFieldByIdAndOwnerName(id, principal.getName());
        if (field == null) {
            throw new ResourceNotFoundException("Field not found or not owned by current user");
        }

        // Nạp dữ liệu cần thiết cho form
        model.addAttribute("field", field);
        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("fieldTypes", fieldTypeService.findAll());
        model.addAttribute("features", featureService.getListFeatures());

        List<Long> selectedFeatureIds = fieldHasFeatureService.findFieldHasFeatureByFieldIdByFieldId(field.getId()).stream()
                .map(FieldHasFeatureResponse::getFeature_id)
                .toList();
        model.addAttribute("selectedFeatureIds", selectedFeatureIds);

        Map<Long, FieldServiceItemResponse> fieldServiceItemMap = fieldServiceItemService.getAllByField(field.getId())
                .stream()
                .collect(Collectors.toMap(FieldServiceItemResponse::getServiceItemId, fsi -> fsi));
        List<Long> selectedServiceIds = fieldServiceItemService.getAllByField(field.getId())
                .stream()
                .map(FieldServiceItemResponse::getServiceItemId)
                .toList();
        model.addAttribute("serviceItems", serviceItemService.findAll());
        model.addAttribute("selectedServiceIds", selectedServiceIds);
        model.addAttribute("fieldServiceItemMap", fieldServiceItemMap);

        model.addAttribute("wards", field.getWardResponse());

        model.addAttribute("subFields",subFieldService.getSubFieldsByFieldId(field.getId()));

        model.addAttribute("content", "fields-update");
        model.addAttribute("currentUri", "/owner/fields/"+ id+ "edit/" );

        return "owner/fields/update";
    }

    @PostMapping("/update")
    public String updateField(
            @ModelAttribute FieldCreateRequest request,
            @RequestParam(value = "image", required = false) List<MultipartFile> images,
            @RequestParam("serviceItemsJson") String serviceItemsJson,
            @RequestParam("id") Long id,
            Principal principal
    ) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        List<ServiceItemsRequest> serviceItems =
                Arrays.asList(mapper.readValue(serviceItemsJson, ServiceItemsRequest[].class));
        request.setServiceItems(serviceItems);

        // Upload ảnh mới (nếu có)
        if (images != null && !images.isEmpty() && !images.get(0).isEmpty()) {
            String uploadDir = new File("src/main/resources/static/web/img/field/FieldType" + request.getField_type()).getAbsolutePath();
            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }

            for (MultipartFile file : images) {
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
                    String imageUrl = "/web/img/field/FieldType" + request.getField_type() + "/" + uniqueName;
                    request.setBanner(imageUrl);
                }
            }
        }

        fieldService.updateFieldByOwner(request, principal.getName(), id);
        return "redirect:/owner/fields";
    }

}
