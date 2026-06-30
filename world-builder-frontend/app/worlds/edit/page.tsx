"use client";

import { useEffect, useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import { getWorld, updateWorld } from "@/actions/worldActions";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Label } from "@/components/ui/label";

export default function EditWorldPage() {
  const router = useRouter();
  const params = useSearchParams();
  const worldId = params.get("worldId");

  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function load() {
      if (!worldId) return;

      const world = await getWorld(worldId);
      setTitle(world.title);
      setDescription(world.description || "");
      setLoading(false);
    }

    load();
  }, [worldId]);

  async function onSave(e: React.FormEvent) {
    e.preventDefault();

    await updateWorld(worldId!, {
      title,
      description,
    });

    router.push(`/worlds/${worldId}`);
  }

  if (loading) {
    return <p className="text-muted-foreground">Loading world...</p>;
  }

  return (
    <div className="max-w-xl mx-auto">
      <Card>
        <CardHeader>
          <CardTitle>Edit World</CardTitle>
          <CardDescription>
            Update your world details
          </CardDescription>
        </CardHeader>

        <CardContent>
          <form onSubmit={onSave} className="space-y-6">
            <div className="space-y-2">
              <Label>World name</Label>
              <Input value={title} onChange={(e) => setTitle(e.target.value)} />
            </div>

            <div className="space-y-2">
              <Label>Description</Label>
              <Textarea
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                rows={5}
              />
            </div>

            <Button className="w-full">Save changes</Button>
          </form>
        </CardContent>
      </Card>
    </div>
  );
}