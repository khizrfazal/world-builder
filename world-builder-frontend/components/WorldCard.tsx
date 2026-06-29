"use client";

import Link from "next/link";
import { useState } from "react";
import { deleteWorld } from "@/actions/worldActions";
import { useRouter } from "next/navigation";

export default function WorldCard({ world }) {
  const router = useRouter();
  const [open, setOpen] = useState(false);
  const [loading, setLoading] = useState(false);

  async function handleDelete(e) {
    e.preventDefault();
    e.stopPropagation();

    setLoading(true);
    await deleteWorld(world.id);
    setLoading(false);

    setOpen(false);
    router.refresh();
  }

  return (
    <>
      <div
        className="
          group block p-5 rounded-xl border border-stone-200 bg-white
          hover:shadow-md hover:-translate-y-0.5 transition-all duration-200
          relative
        "
      >
        <div className="flex items-start justify-between">
          <Link
            href={`/worlds/${world.id}`}
            className="flex-1 pr-10"
          >
            <h2 className="text-lg font-semibold text-stone-900">
              {world.title}
            </h2>

            <p className="text-sm text-stone-500 mt-2 line-clamp-2">
              {world.description || "No description yet"}
            </p>
          </Link>
          <button
            onClick={(e) => {
              e.preventDefault();
              e.stopPropagation();
              setOpen(true);
            }}
            className="
              text-red-500 hover:text-red-700
              transition p-2 rounded-lg hover:bg-red-50
            "
            aria-label="Delete world"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              strokeWidth={1.5}
              stroke="currentColor"
              className="w-5 h-5"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                d="M14.74 9l-.346 9m-4.788 0L9.26 9m9.968-3.21
                c.342.052.682.107 1.022.166m-1.022-.165L18.16
                19.673a2.25 2.25 0 01-2.244
                2.077H8.084a2.25 2.25 0 01-2.244-2.077L4.772
                5.79m14.456 0a48.108 48.108 0 00-3.478-.397
                m-12 .562c.34-.059.68-.114
                1.022-.165m0 0a48.11 48.11 0 013.478-.397m7.5
                0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964
                51.964 0 00-3.32 0c-1.18.037-2.09 1.022-2.09
                2.201v.916m7.5 0a48.667 48.667 0 00-7.5 0"
              />
            </svg>
          </button>
        </div>
      </div>
      {open && (
        <div className="fixed inset-0 z-50 flex items-center justify-center">
          <div
            className="absolute inset-0 bg-black/40"
            onClick={() => setOpen(false)}
          />
          <div className="relative bg-white rounded-xl shadow-xl w-full max-w-md p-6">
            <h2 className="text-lg font-semibold text-stone-900">
              Delete world?
            </h2>

            <p className="text-sm text-stone-500 mt-2">
              This action cannot be undone. The world will be permanently removed.
            </p>

            <div className="flex justify-end gap-3 mt-6">
              <button
                onClick={() => setOpen(false)}
                className="px-4 py-2 text-sm rounded-lg border hover:bg-stone-50"
              >
                Cancel
              </button>

              <button
                onClick={handleDelete}
                disabled={loading}
                className="
                  px-4 py-2 text-sm rounded-lg
                  bg-red-600 text-white
                  hover:bg-red-700
                  disabled:opacity-50
                "
              >
                {loading ? "Deleting..." : "Delete"}
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}