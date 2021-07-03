package murach.cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import murach.business.Cart;
import murach.business.LineItem;
import murach.data.ProductIO;

import java.io.IOException;

@WebServlet(name = "CartServlet", value = "/cart")
public class CartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        var url = "/index.jsp";
        var servletContext = getServletContext();

        // get current action
        var action = request.getParameter("action");
        if (action == null) {
            action = "cart";  // default action
        }

        // perform action and set URL to appropriate page
        switch (action) {
            case "shop":
                url = "/index.jsp";    // the "index" page
                break;
            case "cart":
                var productCode = request.getParameter("productCode");
                var quantityString = request.getParameter("quantity");

                var session = request.getSession();
                var cart = (Cart) session.getAttribute("cart");
                if (cart == null) {
                    cart = new Cart();
                }

                //if the user enters a negative or invalid quantity,
                //the quantity is automatically reset to 1.
                int quantity;
                try {
                    quantity = Integer.parseInt(quantityString);
                    if (quantity < 0) {
                        quantity = 1;
                    }
                } catch (NumberFormatException nfe) {
                    quantity = 1;
                }

                var path = servletContext.getRealPath("/WEB-INF/products.txt");
                var product = ProductIO.getProduct(productCode, path);

                var lineItem = new LineItem(product, quantity);
                if (quantity > 0) {
                    cart.addItem(lineItem);
                } else if (quantity == 0) {
                    cart.removeItem(lineItem);
                }

                session.setAttribute("cart", cart);
                url = "/cart.jsp";
                break;

            case "checkout":
                url = "/checkout.jsp";
                break;
        }

        servletContext.getRequestDispatcher(url).forward(request, response);
    }
}