package cn.farwalker.ravv.service.payment.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StripeCallbackServiceImpl implements StripeCallbackService {

    @Override
    public void doSuccess(Long orderId) {
        
    }
}
