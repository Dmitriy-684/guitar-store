package ru.bonch.guitarstore.service.interfaces;

import ru.bonch.guitarstore.dto.OrderDto;
import ru.bonch.guitarstore.dto.request.ChangeOrderStatusRequest;
import ru.bonch.guitarstore.dto.request.SaveOrderRequest;

import java.util.List;

public interface OrderService {

    OrderDto save(SaveOrderRequest request);

    void changeStatus(ChangeOrderStatusRequest request);

    List<OrderDto> getAll();
}
