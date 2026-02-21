package galand.projects.produits_jardinage.exception;

public class DuplicateProductException extends ProductException{
    public DuplicateProductException(int id)
    {
        super(String.format("Le produit avec l'ID %d existe deja dans notre base de donnees", id), "DUPLICATE_PRODUCT_EXCEPTION");
    }

    public DuplicateProductException(String productName)
    {
        super(String.format("Le produit avec le nom %s existe deja dans notre base de donnees", productName), "DUPLICATE_PRODUCT_EXCEPTION");
    }
}
