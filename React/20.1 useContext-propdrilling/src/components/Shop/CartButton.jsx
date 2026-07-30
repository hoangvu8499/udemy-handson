import { useContext } from "react";
import CartContext from "./CartContext";

export default function CartButton() {
  const {cartCount, handleCart} = useContext(CartContext);
  return (
    <div>
      <p>total product: {cartCount}</p>
      <button onClick={handleCart}>Thêm vào giỏ hàng</button>
    </div>
  );
}
