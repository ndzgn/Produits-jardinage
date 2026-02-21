package galand.projects.produits_jardinage.exception;

import galand.projects.produits_jardinage.entity.Product;

public class ProductException extends RuntimeException{
    public String errorCode;

    public ProductException(String errorMessage, String errorCode)
    {
        super(errorMessage);
        this.errorCode = errorCode;
    }

    public ProductException(String errorMessage, String errorCode, Throwable cause)
    {
        super(errorMessage, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode()
    {
        return this.errorCode;
    }
}
