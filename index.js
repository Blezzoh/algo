var C = [6, 3, 5, 2, 4]
var l = 10
// table of values
const makeTable = (c, l, n) => {
  var T = []
  for (let i = 0; i < n; i++) {
    T[i] = []
  }
  for (let i = 0; i < n; i++) {
    n_char = 0
    for (j = i; j < n; j++) {
      T[i][j] = Number.POSITIVE_INFINITY
      if (j === i)
        n_char += c[j]
      else
        n_char += c[j] + 1
      if (n_char <= l)
        T[i][j] = Math.pow(l - n_char, 2)
    }
  }
  return T
}


const pretty_print = (c, l, n) => {
  var T = makeTable(c, l, n);
  var r = Array(n)
  r[n - 1] = T[n - 1][n - 1]
  for (let i = n - 1; i >= 0; i--) {0
    r[i] = T[i][n - 1];
    for (j = n - 2; j >= i; j--) {
      // if(j==i)
      //   n_char += c[j]
      // else 
      //   n_char += c[j]+1
      // if(r[i] > r[j]){
      // }

      // chars[i] = l - n_char
      r[i] = Math.min(T[i][j] + r[j+1], r[i])
    }
    console.log(r)
  }
  return { r, T }
}


console.log(pretty_print(C, l, C.length))