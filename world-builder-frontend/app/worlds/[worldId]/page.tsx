import { getWorld } from "@/actions/worldActions";
import { getCharacters } from "@/actions/characterActions";
import { World } from "@/types/World";
import { Character } from "@/types/Character";
import CharacterSection from "@/components/CharacterSection";

export const dynamic = "force-dynamic";

export default async function WorldPage({ params }) {
  const world: World = await getWorld(params.worldId);
  const characters: Character[] = await getCharacters(params.worldId);

  return (
    <div className="max-w-3xl mx-auto space-y-8">
      <div>
        <h1 className="text-3xl font-bold">
          {world.title}
        </h1>
        <p className="text-stone-500 mt-2">
          {world.description || "No description yet"}
        </p>
      </div>
      <CharacterSection worldId={params.worldId} characters={characters} />
      <div className="grid gap-4">
        <div className="p-4 bg-white rounded-xl border">
          Lore (coming soon)
        </div>
        <div className="p-4 bg-white rounded-xl border">
          Relationships (coming soon)
        </div>
      </div>
    </div>
  );