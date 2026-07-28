export default function TabButton({children, isSelected, ...props}) {

    return (<>
        <li>
            <button className={isSelected ? "active" : "undefine"} 
                   {...props}>{children}</button>
        </li>
    </>
    );
}