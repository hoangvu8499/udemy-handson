export default function Section({title, children, ...props}) {
    //props tự động lắng nghe thuộc tính/sự kiện đã có của html
    return(
        <>
            <section {...props}>
                <h2>{title}</h2>
                {children}
            </section>
        </>
    )
}