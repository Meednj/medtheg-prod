import { useEffect, useRef, type MouseEvent, type ReactNode } from 'react'

type DialogProps = {
  open: boolean
  onOpenChange: (open: boolean) => void
  label: string
  children: ReactNode
  className?: string
}

export function Dialog({ open, onOpenChange, label, children, className = '' }: DialogProps) {
  const dialogRef = useRef<HTMLDialogElement>(null)

  useEffect(() => {
    const dialog = dialogRef.current
    if (!dialog) return
    if (open && !dialog.open) dialog.showModal()
    if (!open && dialog.open) dialog.close()
  }, [open])

  function handleBackdropClick(event: MouseEvent<HTMLDialogElement>) {
    if (event.target === event.currentTarget) onOpenChange(false)
  }

  return (
    <dialog
      ref={dialogRef}
      className={`dialog ${className}`.trim()}
      aria-label={label}
      onCancel={() => onOpenChange(false)}
      onClose={() => onOpenChange(false)}
      onClick={handleBackdropClick}
    >
      {children}
    </dialog>
  )
}
