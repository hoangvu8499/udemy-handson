import { useTranslation } from "react-i18next";

export default function LanguageSwitcher() {

    const { i18n } = useTranslation();

    const changeLanguage = (lang) => {
        i18n.changeLanguage(lang);
        localStorage.setItem("language", lang);
    };

    return (
        <>
            <div className="language-switcher">
                <button onClick={() => changeLanguage("vi")}>
                    🇻🇳
                </button>

                <button onClick={() => changeLanguage("en")}>
                    🇺🇸
                </button>
            </div>

        </>
    );
}