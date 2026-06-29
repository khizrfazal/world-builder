import Link from "next/link";

export default function WorldCard({ world }) {
  return (
    <Link
      href={`/worlds/${world.id}`}
      className="block w-full p-4 bg-white rounded-lg shadow hover:shadow-md transition"
    >
      <h2 className="text-lg font-semibold">
        {world.title}
      </h2>
      <p className="text-sm text-stone-500 mt-1">
        {world.description || "No description"}
      </p>
    </Link>
  );
}