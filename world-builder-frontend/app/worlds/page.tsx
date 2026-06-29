import Link from "next/link";

import WorldCard from "@/components/WorldCard";
import EmptyState from "@/components/EmptyState";
import { getWorlds } from "@/actions/worldActions";
import { World } from "@/types/World";

export const dynamic = "force-dynamic";

export default async function WorldsPage() {
  const worlds: World[] = await getWorlds();

  return (
    <div className="max-w-2xl mx-auto space-y-6">
      <div>
        <h1 className="text-3xl font-bold">Your Worlds</h1>
        <p className="text-gray-500">
          Create and manage your story universes
        </p>
      </div>
      {worlds.length === 0 ? (
        <div className="space-y-2 text-gray-500">
          <p className="text-lg font-medium text-stone-900">
            No worlds yet
          </p>
          <p>
            Create your first world to begin building your universe.
          </p>
        </div>
      ) : (
        <div className="space-y-3">
          {worlds.map((w) => (
            <WorldCard key={w.id} world={w} />
          ))}
        </div>
      )}
      <div className="pt-4">
        <Link
          href="/worlds/add"
          className="inline-block bg-black text-white px-4 py-2 rounded"
        >
          Create World
        </Link>
      </div>
    </div>
  );
}