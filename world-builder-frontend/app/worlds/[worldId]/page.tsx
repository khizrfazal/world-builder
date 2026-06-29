export default function WorldPage({ params }) {
  return (
    <div className="p-6">
      <h1 className="text-3xl font-bold">
        World: {params.worldId}
      </h1>

      <div className="mt-6 space-y-3">
        <div className="p-4 bg-white rounded shadow">
          Characters (coming soon)
        </div>

        <div className="p-4 bg-white rounded shadow">
          Lore (coming soon)
        </div>

        <div className="p-4 bg-white rounded shadow">
          Relationships (coming soon)
        </div>
      </div>
    </div>
  );
}