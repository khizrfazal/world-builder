import "./globals.css";
import { Geist } from "next/font/google";
import { cn } from "@/lib/utils";

const geist = Geist({
  subsets: ["latin"],
  variable: "--font-sans",
});

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en" className={cn("font-sans", geist.variable)}>
      <body className="min-h-screen bg-zinc-50 text-zinc-900 antialiased">
        <div className="fixed inset-0 -z-10">
          <div className="absolute inset-0 bg-gradient-to-b from-white via-zinc-50 to-zinc-100" />
          <div className="absolute top-[-200px] left-1/2 -translate-x-1/2 h-[400px] w-[800px] rounded-full bg-zinc-200 blur-3xl opacity-40" />
        </div>
        <div className="mx-auto max-w-6xl px-6 py-10">
          {children}
        </div>
      </body>
    </html>
  );
}