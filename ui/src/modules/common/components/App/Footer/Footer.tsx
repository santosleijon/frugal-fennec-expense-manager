import { Typography, Link } from "@material-ui/core"
import React from "react"
import './Footer.css'

function Footer() {
  return (
    <footer className="Footer">
      <Typography variant="subtitle1" align="center" color="textSecondary" component="p">
        Check out the <Link color="inherit" href="http://github.com/santosleijon/frugal-fennec-expense-manager">Frugal Fennec source code on GitHub</Link>
      </Typography>
      <Typography variant="body2" color="textSecondary" align="center">
        {'Copyright © '}
        <Link color="inherit" href="http://github.com/santosleijon/" target="_blank">
          Santos Leijon
        </Link>{' '}
        {new Date().getFullYear()}
      </Typography>
    </footer>
  )
}

export default Footer