package galand.projects.produits_jardinage.exception;

public class InvalidPriceException extends ProductException{
    public InvalidPriceException(String message)
    {
        super(message, "INVALID_PRICE_EXCEPTION");
    }
}
