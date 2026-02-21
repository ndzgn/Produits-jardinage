package galand.projects.produits_jardinage.exception;

public class ProductNotFoundException extends ProductException{
    public ProductNotFoundException(int id)
    {
        super("Produit non trouve avec l'ID "+ id, "PRODUCT_NOT_FOUD");
    }
}
