export default function TabButton({children, onSelect, isSelected}) {

    return (<>
        <li>
            <button className={isSelected ? "active" : "undefine"} 
                    onClick={onSelect}>{children}</button>
        </li>
    </>
    );
}