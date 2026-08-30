package com.bookstore.onlinebookstore.cart.dto;

import java.math.BigDecimal;

public record CartItemResponse(Long bookId,String title,BigDecimal price,Integer quantity, BigDecimal subTotal) {

}
