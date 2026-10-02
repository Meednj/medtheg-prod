type EditorialRuleProps = {
  orientation?: 'horizontal' | 'vertical'
  extend?: boolean
  interruption?: boolean
  className?: string
}

export function EditorialRule({ orientation = 'horizontal', extend = false, interruption = false, className = '' }: EditorialRuleProps) {
  const classes = ['editorial-rule', `editorial-rule--${orientation}`, extend && 'is-extended', interruption && 'editorial-rule--interruption', className]
    .filter(Boolean)
    .join(' ')
  return <span className={classes} aria-hidden="true" />
}
