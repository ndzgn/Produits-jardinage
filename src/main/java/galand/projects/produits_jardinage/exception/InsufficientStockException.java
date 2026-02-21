package galand.projects.produits_jardinage.exception;

public class InsufficientStockException extends ProductException{
    public InsufficientStockException(String productName, Integer requested, Integer avaible)
    {
        super(String.format("Stock insuffisant pour %s. Demande: %d, Disponible: %d", productName, requested, avaible), "INSUFFICIENT_STOCK");
    }
}
