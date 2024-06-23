package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.ItemMaskService;
import com.smart.tailor.service.PartOfDesignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(APIConstant.ItemMaskAPI.ITEM_MASK)
@RequiredArgsConstructor
@Slf4j
public class ItemMaskController {
    private final ItemMaskService itemMaskService;
    private final Logger logger = LoggerFactory.getLogger(MaterialController.class);
    private final ObjectMapper objectMapper;

    @GetMapping(APIConstant.ItemMaskAPI.GET_ALL_ITEM_MASK_BY_PART_OF_DESIGN_ID + "/{partOfDesignID}")
    public ResponseEntity<ObjectNode> getAllItemMaskByPartOfDesignID(@PathVariable("partOfDesignID") UUID partOfDesignID) {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var itemMaskResponses = itemMaskService.getListItemMaskByPartOfDesignID(partOfDesignID);
            if(itemMaskResponses.isEmpty()){
                response.put("status", HttpStatus.OK.value());
                response.put("message", MessageConstant.CAN_NOT_FIND_ANY_ITEM_MASK_BY_PART_OF_DESIGN_ID);
            }else{
                response.put("status", HttpStatus.OK.value());
                response.put("message", MessageConstant.GET_ALL_ITEM_MASK_BY_PART_OF_DESIGN_ID_SUCCESSFULLY);
                response.set("data", objectMapper.valueToTree(itemMaskResponses));
            }
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN GET ALL ITEM MASK BY PART OF DESIGN ID. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }


    @GetMapping(APIConstant.ItemMaskAPI.GET_ALL_ITEM_MASK)
    public ResponseEntity<ObjectNode> getAllItemMask() {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var itemMaskResponses = itemMaskService.getAllItemMask();
            if(itemMaskResponses.isEmpty()){
                response.put("status", HttpStatus.OK.value());
                response.put("message", MessageConstant.CAN_NOT_FIND_ANY_ITEM_MASK);
            }else{
                response.put("status", HttpStatus.OK.value());
                response.put("message", MessageConstant.GET_ALL_ITEM_MASK_SUCCESSFULLY);
                response.set("data", objectMapper.valueToTree(itemMaskResponses));
            }

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN GET ALL ITEM MASK. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping(APIConstant.ItemMaskAPI.GET_ITEM_MASK_BY_ID + "/{itemMaskID}")
    public ResponseEntity<ObjectNode> getDesignByID(@PathVariable("itemMaskID") UUID itemMaskID) {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var itemMaskResponse = itemMaskService.getItemMaskByItemMaskID(itemMaskID);
            if(itemMaskResponse == null){
                response.put("status", HttpStatus.OK.value());
                response.put("message", MessageConstant.CAN_NOT_FIND_ANY_ITEM_MASK);
            }else{
                response.put("status", HttpStatus.OK.value());
                response.put("message", MessageConstant.GET_ITEM_MASK_BY_ID_SUCCESSFULLY);
                response.set("data", objectMapper.valueToTree(itemMaskResponse));
            }

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN GET ITEM MASK BY ID. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}
