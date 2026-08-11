import './Pagination.css'


export default function Pagination({
    currentPage,
    totalPages,
    setPageNumber
}) {
    return (
        <div className="pagination">
            <button
                disabled={currentPage === 0}
                onClick={() => setPageNumber(currentPage - 1)}
            >
                Previous
            </button>

            {[...Array(totalPages)].map((_, index) => (
                <button
                    key={index}
                    className={
                        currentPage === index
                            ? "page-btn active"
                            : "page-btn"
                    }
                    onClick={() => setPageNumber(index)}
                >
                    {index + 1}
                </button>
            ))}

            <button
                disabled={currentPage === totalPages - 1}
                onClick={() => setPageNumber(currentPage + 1)}
            >
                Next
            </button>
        </div>
    );
}