package org.clevernameinc.itnfinal.db;

import org.clevernameinc.itnfinal.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

///
/// @brief
///     Loads products from a remote database
///
public class ProductLoader {

    public static ArrayList<Product> loadProducts(Statement stmt) throws SQLException {
        String selection = "SELECT * FROM products";
        ResultSet res = stmt.executeQuery(selection);

        ArrayList<Product> out = new ArrayList<>();

        while(res.next()) {
            Product p = new Product(res.getInt("p_id"));

            p.setDesc(res.getString("p_desc"));
            p.setBrand(res.getString("p_brand"));
            p.setPrice(res.getDouble("p_price"));
            p.setWeight(res.getDouble("p_weight"));
            p.setRating(res.getDouble("p_rating"));

            out.add(p);
        }

        return out;
    }

}
