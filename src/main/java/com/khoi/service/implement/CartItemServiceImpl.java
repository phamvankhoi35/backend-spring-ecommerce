package com.khoi.service.implement;

import com.khoi.entity.CartItem;
import com.khoi.entity.User;
import com.khoi.repository.CartItemRepository;
import com.khoi.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem updateCartItem(Long userId, Long cartItemId, CartItem cartItem) throws Exception {
        CartItem item = getCartItemById(cartItemId);
        User cartItemUSer = item.getCart().getUser();

        if(cartItemUSer.getId().equals(userId)) {
            item.setQuantity(cartItem.getQuantity());
            item.setMrpPrice(item.getQuantity() * item.getProduct().getMrpPrice());
            item.setSellingPrice(item.getQuantity() * item.getProduct().getSellingPrice());

            return cartItemRepository.save(item);
        }

        throw  new Exception("You can not update this cart item");
//        return null;
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws Exception {
        CartItem item = getCartItemById(cartItemId);
        User cartItemUSer = item.getCart().getUser();

        if(cartItemUSer.getId().equals(userId)) {
            cartItemRepository.delete(item);
        } else {
            throw new Exception("You can not delete this item");
        }
    }

    @Override
    public CartItem getCartItemById(Long id) throws Exception {
        return cartItemRepository
                .findById(id)
                .orElseThrow(() -> new Exception("Cart item {" + id + "} not found"));
    }
}
