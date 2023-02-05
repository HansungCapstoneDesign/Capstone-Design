import React from "react";
import { menuItems } from "../data/MenuItems";
import { RxAvatar } from "react-icons/rx";
import "../style/Home.css";

const Navbar: React.FC = () => {
    return (
        <div>
            <nav className="navigation">
            <ul>
            {menuItems.map((menu, index) => {
                return (
                    <li key={index}>{menu.title} </li>
                );
            })}
            </ul>
            <RxAvatar size={35} className="myPageIcon"/>
            </nav>
        </div>
    );
  }
  
  export default Navbar;