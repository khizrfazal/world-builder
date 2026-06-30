"use client";

import { useRouter } from "next/navigation";
import { wbClient } from "@/utils/client";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { useState } from "react";

export default function EditWorldForm({ worldId, world }) {
  const router = useRouter();
  const [loading, setLoading] = useState(false);

  async function onSubmit(e) {
    e.preventDefault();
    setLoading(true);

    const formData = new FormData(e.currentTarget);

    await wbClient.put(`/worlds/${worldId}`, {
      title: formData.get("title"),
      description: formData.get("description"),
    });

    router.push(`/worlds/${worldId}`);
  }

  return (
    <Card>
      <CardHeader>
        <CardTitle>Edit World</CardTitle>
        <CardDescription>Update your world’s details.</CardDescription>
      </CardHeader>

      <CardContent>
        <form onSubmit={onSubmit} className="space-y-6">
          <div className="space-y-2">
            <Label htmlFor="title">World name</Label>
            <Input id="title" name="title" defaultValue={world.title} required />
          </div>

          <div className="space-y-2">
            <Label htmlFor="description">Description</Label>
            <Textarea
              id="description"
              name="description"
              defaultValue={world.description || ""}
              rows={5}
            />
          </div>

          <div className="flex justify-center pt-4">
            <Button
              type="submit"
              className="bg-black text-white hover:bg-black/90 px-8 py-3 font-semibold"
              disabled={loading}
            >
              {loading ? "Saving..." : "Save Changes"}
            </Button>
          </div>
        </form>
      </CardContent>
    </Card>
  );
}