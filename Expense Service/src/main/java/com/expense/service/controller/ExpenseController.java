package com.expense.service.controller;

import com.expense.service.dto.ExpenseDto;
import com.expense.service.service.ExpenseService;
import jakarta.websocket.server.PathParam;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expense/v1")
public class ExpenseController {

    private ExpenseService expenseService;

    @Autowired
    ExpenseController(ExpenseService expenseService){
        this.expenseService = expenseService;
    }

    @GetMapping("/getexpense")
    public ResponseEntity<List<ExpenseDto>> getExpenses(@RequestParam("user_id") @NonNull String userId){
        try {
            List<ExpenseDto> expenseDtoList = expenseService.getExpenses(userId);
            return new ResponseEntity<>(expenseDtoList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/addexpense")
    public ResponseEntity<Boolean> addExpenses( @RequestHeader(value = "X-User-Id") @NonNull String userId,
                                                @RequestBody ExpenseDto expenseDto){
        try {
            System.out.println("Header user id "+userId);
            expenseDto.setUserId(userId);
            System.out.println("Expense dto user id"+ expenseDto.getUserId());
            return new ResponseEntity<>(expenseService.createExpense(expenseDto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
        }
    }
}
