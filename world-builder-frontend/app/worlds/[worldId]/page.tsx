import { getWorld } from "@/actions/worldActions";
import { World } from "@/types/World"

export default async function WorldPage({ params }) {
  const world: World = await getWorld(params.worldId);
  return (
    <div className="max-w-3xl mx-auto space-y-6">
      <div>
        <h1 className="text-3xl font-bold">
          {world.title}
        </h1>
        <p className="text-stone-500 mt-2">
          {world.description || "No description yet"}
        </p>
      </div>
      <div className="grid gap-4 mt-8">
        <div className="p-4 bg-white rounded-xl border">
          Characters (coming soon)
        </div>
        <div className="p-4 bg-white rounded-xl border">
          Lore (coming soon)
        </div>
        <div className="p-4 bg-white rounded-xl border">
          Relationships (coming soon)
        </div>
      </div>
    </div>
  );
}