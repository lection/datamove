/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import com.linkin.crm.product.model.ProductCategory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public class ProductCategoryUtil {
    private Map<String,ProductCategory> proMap;

    public ProductCategoryUtil(Connection conn,String org_name) {
        proMap = (Map)DBUtil.executeQuery(conn,
                "select p.* from t_productcategory p,t_organization o where o.c_name='"+org_name
                +"' and o.c_id=p.c_org_id and p.c_code like '%-%'"
        , new DBUtil.Query() {
            public Object execute(ResultSet rs) throws SQLException {
                Map<String,ProductCategory> map = new HashMap<String, ProductCategory>(20);
                ProductCategory pro = null;
                String code = null;
                while(rs.next()){
                    pro = new ProductCategory();
                    pro.setId(rs.getLong("c_id"));
                    code = rs.getString("c_code");
                    map.put(code.substring(code.lastIndexOf("-")), pro);
                }
                return map;
            }
        });
        DBUtil.close(conn);
    }
    public ProductCategory getProductCategory(String pid){
        return proMap.get("-" + pid);
    }
}
