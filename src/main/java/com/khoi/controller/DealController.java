package com.khoi.controller;

import com.khoi.dto.response.ApiResponse;
import com.khoi.entity.Deal;
import com.khoi.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/deal")
public class DealController {
    private final DealService dealService;

    @PostMapping
    public ResponseEntity<Deal> createDeal(@RequestBody Deal deal) {
        Deal createDeal = dealService.createDeal(deal);

        return new ResponseEntity<>(createDeal,HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Deal> updateDeal(
            @PathVariable Long id,
            @RequestBody Deal deal
    ) throws Exception {
        Deal update = dealService.updateDeal(deal, id);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteDeal(@PathVariable Long id) throws Exception {
        dealService.deleteDeal(id);
        ApiResponse res = new ApiResponse<>();
        res.setMessage("Deal has been deleted");
        res.setStatus(200);

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }
}
