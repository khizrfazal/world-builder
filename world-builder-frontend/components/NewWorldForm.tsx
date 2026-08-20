"use client";

import { useState } from "react";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";

export default function NewWorldForm({ action }) {
  const [loading, setLoading] = useState(false);

  async function onSubmit(formData: FormData) {
    setLoading(true);
    await action(formData);
  }

  return (
    <Card>
      <CardHeader>
        <CardTitle>World Details</CardTitle>
        <CardDescription>
          Give your world a name and a short description.
        </CardDescription>
      </CardHeader>

      <CardContent>
        <form action={onSubmit} className="space-y-6">
          <div className="space-y-2">
            <Label htmlFor="title">World name</Label>
            <Input id="title" name="title" required />
          </div>

          <div className="space-y-2">
            <Label htmlFor="description">Description</Label>
            <Textarea
              id="description"
              name="description"
              placeholder="Describe your world..."
              rows={5}
            />
          </div>

          <div className="flex justify-center pt-4">
            <Button
              type="submit"
              disabled={loading}
              className="bg-black text-white hover:bg-black/90 px-8 py-3 font-semibold"
            >
              {loading ? "Creating..." : "Create World"}
            </Button>
          </div>
        </form>
      </CardContent>
    </Card>
  );
}