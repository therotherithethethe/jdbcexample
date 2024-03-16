package persistance.entity.handler.producthandler;

import persistance.entity.Product;

public interface ProductHandler{
    public void validate(Product product);
    public void setNextHandler(ProductHandler productHandler);

}
