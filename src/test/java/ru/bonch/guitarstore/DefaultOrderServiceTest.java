package ru.bonch.guitarstore;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bonch.guitarstore.dto.OrderDto;
import ru.bonch.guitarstore.dto.request.ChangeOrderStatusRequest;
import ru.bonch.guitarstore.dto.request.SaveOrderRequest;
import ru.bonch.guitarstore.entity.GuitarEntity;
import ru.bonch.guitarstore.entity.OrderEntity;
import ru.bonch.guitarstore.entity.UserEntity;
import ru.bonch.guitarstore.enums.OrderStatus;
import ru.bonch.guitarstore.enums.UserRole;
import ru.bonch.guitarstore.repository.OrderRepository;
import ru.bonch.guitarstore.service.impl.DefaultOrderService;
import ru.bonch.guitarstore.service.interfaces.GuitarService;
import ru.bonch.guitarstore.service.interfaces.UserService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DefaultOrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserService userService;

    @Mock
    private GuitarService guitarService;

    @InjectMocks
    private DefaultOrderService orderService;

    @Test
    void save_ShouldSaveOrderAndReturnDto() {
        // Arrange
        SaveOrderRequest request = new SaveOrderRequest("user-id", "guitar-id");

        UserEntity userEntity = UserEntity.builder()
                .userId("user-id")
                .username("username")
                .password("password")
                .fullName("John Doe")
                .phone("1234567890")
                .role(UserRole.USER)
                .build();

        GuitarEntity guitarEntity = GuitarEntity.builder()
                .guitarId("guitar-id")
                .model("Guitar Model")
                .price(1000)
                .description("Guitar Description")
                .build();

        String orderId = "generated-order-id";
        OrderEntity savedEntity = OrderEntity.builder()
                .orderId(orderId)
                .user(userEntity)
                .guitar(guitarEntity)
                .status(OrderStatus.PENDING)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        Mockito.when(userService.getEntityByUserId("user-id")).thenReturn(userEntity);
        Mockito.when(guitarService.getEntityByGuitarId("guitar-id")).thenReturn(guitarEntity);
        Mockito.when(orderRepository.save(Mockito.any(OrderEntity.class))).thenReturn(savedEntity);

        // Act
        OrderDto result = orderService.save(request);

        // Assert
        ArgumentCaptor<OrderEntity> captor = ArgumentCaptor.forClass(OrderEntity.class);
        Mockito.verify(orderRepository).save(captor.capture());

        OrderEntity capturedEntity = captor.getValue();
        Assertions.assertNotNull(capturedEntity.getOrderId());
        Assertions.assertEquals("user-id", capturedEntity.getUser().getUserId());
        Assertions.assertEquals("guitar-id", capturedEntity.getGuitar().getGuitarId());
        Assertions.assertEquals(OrderStatus.PENDING, capturedEntity.getStatus());

        Assertions.assertEquals("1234567890", result.userPhone());
        Assertions.assertEquals("John Doe", result.userFullName());
        Assertions.assertEquals("Guitar Model", result.guitarModel());
        Assertions.assertEquals(1000, result.guitarPrice());
        Assertions.assertEquals(OrderStatus.PENDING, result.status());
    }

    @Test
    void changeStatus_ShouldUpdateOrderStatus() {
        // Arrange
        ChangeOrderStatusRequest request = new ChangeOrderStatusRequest("order-id", OrderStatus.CONFIRMED);

        OrderEntity existingEntity = OrderEntity.builder()
                .orderId("order-id")
                .user(UserEntity.builder().userId("user-id").build())
                .guitar(GuitarEntity.builder().guitarId("guitar-id").build())
                .status(OrderStatus.PENDING)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        Mockito.when(orderRepository.findOrderByOrderId("order-id")).thenReturn(Optional.of(existingEntity));

        // Act
        orderService.changeStatus(request);

        // Assert
        Mockito.verify(orderRepository).save(existingEntity);
        Assertions.assertEquals(OrderStatus.CONFIRMED, existingEntity.getStatus());
    }

    @Test
    void getAll_ShouldReturnListOfOrderDtos() {
        // Arrange
        List<OrderEntity> entities = List.of(
                OrderEntity.builder()
                        .orderId("order-1")
                        .user(UserEntity.builder().userId("user-id-1").fullName("User 1").phone("12345").build())
                        .guitar(GuitarEntity.builder().guitarId("guitar-id-1").model("Guitar 1").price(1000).build())
                        .status(OrderStatus.PENDING)
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build(),
                OrderEntity.builder()
                        .orderId("order-2")
                        .user(UserEntity.builder().userId("user-id-2").fullName("User 2").phone("67890").build())
                        .guitar(GuitarEntity.builder().guitarId("guitar-id-2").model("Guitar 2").price(2000).build())
                        .status(OrderStatus.CONFIRMED)
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build()
        );

        Mockito.when(orderRepository.findAll()).thenReturn(entities);

        // Act
        List<OrderDto> result = orderService.getAll();

        // Assert
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("order-1", result.get(0).orderId());
        Assertions.assertEquals("User 1", result.get(0).userFullName());
        Assertions.assertEquals("Guitar 1", result.get(0).guitarModel());
        Assertions.assertEquals(1000, result.get(0).guitarPrice());
        Assertions.assertEquals(OrderStatus.PENDING, result.get(0).status());

        Assertions.assertEquals("order-2", result.get(1).orderId());
        Assertions.assertEquals("User 2", result.get(1).userFullName());
        Assertions.assertEquals("Guitar 2", result.get(1).guitarModel());
        Assertions.assertEquals(2000, result.get(1).guitarPrice());
        Assertions.assertEquals(OrderStatus.CONFIRMED, result.get(1).status());
    }

    @Test
    void save_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        SaveOrderRequest request = new SaveOrderRequest("nonexistent-user-id", "guitar-id");

        Mockito.when(userService.getEntityByUserId("nonexistent-user-id")).thenThrow(new IllegalStateException("User not found"));

        // Act & Assert
        IllegalStateException exception = Assertions.assertThrows(
                IllegalStateException.class,
                () -> orderService.save(request)
        );

        Assertions.assertEquals("User not found", exception.getMessage());
    }

    @Test
    void save_ShouldThrowException_WhenGuitarNotFound() {
        // Arrange
        SaveOrderRequest request = new SaveOrderRequest("user-id", "nonexistent-guitar-id");

        Mockito.when(guitarService.getEntityByGuitarId("nonexistent-guitar-id")).thenThrow(new IllegalStateException("Guitar not found"));

        // Act & Assert
        IllegalStateException exception = Assertions.assertThrows(
                IllegalStateException.class,
                () -> orderService.save(request)
        );

        Assertions.assertEquals("Guitar not found", exception.getMessage());
    }

    @Test
    void changeStatus_ShouldThrowException_WhenOrderNotFound() {
        // Arrange
        ChangeOrderStatusRequest request = new ChangeOrderStatusRequest("nonexistent-order-id", OrderStatus.CONFIRMED);

        Mockito.when(orderRepository.findOrderByOrderId("nonexistent-order-id")).thenReturn(Optional.empty());

        // Act & Assert
        IllegalStateException exception = Assertions.assertThrows(
                IllegalStateException.class,
                () -> orderService.changeStatus(request)
        );

        Assertions.assertEquals("Order with orderId nonexistent-order-id not found", exception.getMessage());
    }
}

