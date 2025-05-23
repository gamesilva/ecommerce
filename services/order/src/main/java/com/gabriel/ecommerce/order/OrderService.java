package com.gabriel.ecommerce.order;

import com.gabriel.ecommerce.customer.CustomerClient;
import com.gabriel.ecommerce.exception.BusinessException;
import com.gabriel.ecommerce.kafka.OrderConfirmation;
import com.gabriel.ecommerce.kafka.OrderProducer;
import com.gabriel.ecommerce.orderline.OrderLineRequest;
import com.gabriel.ecommerce.orderline.OrderLineService;
import com.gabriel.ecommerce.payment.PaymentClient;
import com.gabriel.ecommerce.payment.PaymentRequest;
import com.gabriel.ecommerce.product.ProductClient;
import com.gabriel.ecommerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;

    public Integer createOrder(@Valid OrderRequest request) {
        // Check the customer --> OpenFeing
        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order:: No customer exists with the provided ID: " + request.customerId()));

        // Purchase the products --> products-ms (RestTemplate)
        var purchasedProducts = this.productClient.purchaseProducts(request.products());

        // Persist Order
        var order = this.repository.save(mapper.toOrder(request));

        // Persist order lines
        for (PurchaseRequest purchaseRequest : request.products()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }

        // Start payment process
        var paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);

        // Send the order confirmation --> notification-ms (Kafka)
        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );
        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return repository
                .findAll()
                .stream()
                .map(mapper::fromOrder)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Integer orderId) {
        return repository
                .findById(orderId)
                .map(mapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No order found with the provided ID: %d", orderId)));
    }
}
