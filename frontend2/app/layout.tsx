import React from "react"
import type { Metadata, Viewport } from 'next'
import { Montserrat, JetBrains_Mono } from 'next/font/google'

import './globals.css'

const _montserrat = Montserrat({ subsets: ['latin'], variable: '--font-montserrat' })
const _jetbrains = JetBrains_Mono({ subsets: ['latin'], variable: '--font-jetbrains' })


export const metadata: Metadata = {
  title: 'Enterprise Portal',
  description: 'Socraft Enterprise Portal - Manage your projects and workspace',
}

export const viewport: Viewport = {
  themeColor: '#0D0D0D',
}

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode
}>) {
  return (
    <html lang="en">
      <body className={`${_montserrat.variable} ${_jetbrains.variable} font-sans antialiased min-h-screen bg-background text-foreground`}>{children}</body>
    </html>
  )
}
