export default function ResultDialog({
  result,
  scores,
  targetTime,
  currentTime,
  ref
}) {
  return (
    <dialog ref={ref} className="result-dialog">
      <h3>{result}</h3>

      <p>
        Your sscores: <strong>{scores}/100</strong>
      </p>

      <p>
        Target time: <strong>{targetTime}s</strong>
      </p>

      <p>
        Your time: <strong>{currentTime}s</strong>
      </p>

      <form method="dialog">
        <button>close</button>
      </form>
    </dialog>
  );
}