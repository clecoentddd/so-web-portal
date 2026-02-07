import { AlertTriangle } from "lucide-react"

interface ErrorBannerProps {
  message: string
}

export function ErrorBanner({ message }: ErrorBannerProps) {
  return (
    <div className="mb-6 flex items-center gap-3 border border-destructive/30 bg-destructive/5 px-4 py-3">
      <AlertTriangle className="h-4 w-4 shrink-0 text-destructive" />
      <p className="text-sm text-destructive">{message}</p>
    </div>
  )
}
