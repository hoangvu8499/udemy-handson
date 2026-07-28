export default function Tabs({button, children, ButtonContainer}) {
    return (
        <>
            <ButtonContainer>
                {button}
            </ButtonContainer>
            {children}
        </>
    )
}