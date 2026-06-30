"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { Button } from "@/components/ui/button";

export function BackLink() {
  const pathname = usePathname();
  const segments = pathname.split("/").filter(Boolean);

  // worlds / {worldId}
  if (segments.length === 2) {
    return (
      <Button asChild variant="ghost" className="mb-6">
        <Link href="/worlds">← Back to all worlds</Link>
      </Button>
    );
  }

  // worlds / {worldId} / {section}
  if (segments.length === 3) {
    const worldId = segments[1];
    return (
      <Button asChild variant="ghost" className="mb-6">
        <Link href={`/worlds/${worldId}`}>← Back to world</Link>
      </Button>
    );
  }

  // worlds / {worldId} / {section} / {entityId}
  if (segments.length === 4) {
    const worldId = segments[1];
    const section = segments[2];
    return (
      <Button asChild variant="ghost" className="mb-6">
        <Link href={`/worlds/${worldId}/${section}`}>
          ← Back to {section.replace("-", " ")}
        </Link>
      </Button>
    );
  }

  // worlds / {worldId} / {section} / {entityId} / {subsection}
  if (segments.length >= 5) {
    const worldId = segments[1];
    const section = segments[2];
    const entityId = segments[3];
    return (
      <Button asChild variant="ghost" className="mb-6">
        <Link href={`/worlds/${worldId}/${section}/${entityId}`}>
          ← Back to {section.replace("-", " ")} details
        </Link>
      </Button>
    );
  }

  return null;
}