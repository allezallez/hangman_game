for f in $(ls [a-z]*); do
  mv $f ${f/yes/true}
done
