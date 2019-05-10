from sympy import evalf, sympify
from mpmath import sin, cos, tan, asin, acos, atan, sqrt
def evaluate(expression):

    expression = sympify(expression)
    return expression.evalf()

