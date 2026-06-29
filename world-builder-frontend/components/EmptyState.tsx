export default function EmptyState({ title, description }) {
  return (
    <div className="text-center py-12 text-gray-500">
      <h2 className="text-lg font-semibold">{title}</h2>
      <p>{description}</p>
    </div>
  );
}