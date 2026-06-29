import Link from "next/link";

import WorldCard from "@/components/WorldCard";
import { getWorlds } from "@/actions/worldActions";
import { World } from "@/types/World";

export const dynamic = "force-dynamic";

export default async function WorldsPage() {
  const worlds: World[] = await getWorlds();

  return (
    <div className="max-w-3xl mx-auto space-y-10">
      <div className="flex items-start justify-between">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">
            Your Worlds
          </h1>
          <p className="text-sm text-stone-500 mt-1">
            Create and manage your story universes
          </p>
        </div>
        <Link
          href="/worlds/add"
          className="bg-black text-white px-4 py-2 rounded-lg text-sm hover:bg-stone-800 transition"
        >
          Create world
        </Link>
      </div>
      {worlds.length === 0 ? (
        <div className="text-center py-16 border border-dashed rounded-xl bg-white">
          <h2 className="text-lg font-semibold text-stone-900">
            No worlds yet
          </h2>
          <p className="text-sm text-stone-500 mt-1">
            Create your first world to start building your universe
          </p>

          <Link
            href="/worlds/add"
            className="inline-block mt-4 bg-black text-white px-4 py-2 rounded-lg text-sm"
          >
            Create your first world
          </Link>
        </div>
      ) : (
        <div className="space-y-4">
          {worlds.map((w) => (
            <WorldCard key={w.id} world={w} />
          ))}
        </div>
      )}
    </div>
  );
}