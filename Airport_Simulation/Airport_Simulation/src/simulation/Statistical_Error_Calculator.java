package simulation;

/*Error Function Class for integration to calculate area*/
public class Statistical_Error_Calculator {
	private static double lnGamma(double alpha) {
		// log Gamma function: ln(gamma(alpha)) for alpha>0, accurate to 10 decimal
		// places
		double x = alpha, f = 0.0, z;

		if (x < 7) {
			f = 1;
			z = x - 1;
			while (++z < 7) {
				f *= z;
			}
			x = z;
			f = -Math.log(f);
		}
		z = 1 / (x * x);

		return f + (x - 0.5) * Math.log(x) - x + 0.918938533204673
				+ (((-0.000595238095238 * z + 0.000793650793651) * z - 0.002777777777778) * z + 0.083333333333333) / x;
	}

	private static double incompleteGamma(double x, double alpha, double ln_gamma_alpha) {

		/*
		 * Returns the incomplete gamma ratio I(x,alpha) where x is the upper limit of
		 * the integration and alpha is the shape parameter.
		 */
		double accurate = 1e-8, overflow = 1e30;
		double factor, gin, rn, a, b, an, dif, term;
		double pn0, pn1, pn2, pn3, pn4, pn5;

		if (x == 0.0) {
			return 0.0;
		}
		if (x < 0.0 || alpha <= 0.0) {
			throw new IllegalArgumentException("Arguments out of bounds");
		}

		factor = Math.exp(alpha * Math.log(x) - x - ln_gamma_alpha);

		if (x > 1 && x >= alpha) {
			// continued fraction
			a = 1 - alpha;
			b = a + x + 1;
			term = 0;
			pn0 = 1;
			pn1 = x;
			pn2 = x + 1;
			pn3 = x * b;
			gin = pn2 / pn3;

			do {
				a++;
				b += 2;
				term++;
				an = a * term;
				pn4 = b * pn2 - an * pn0;
				pn5 = b * pn3 - an * pn1;

				if (pn5 != 0) {
					rn = pn4 / pn5;
					dif = Math.abs(gin - rn);
					if (dif <= accurate) {
						if (dif <= accurate * rn) {
							break;
						}
					}

					gin = rn;
				}
				pn0 = pn2;
				pn1 = pn3;
				pn2 = pn4;
				pn3 = pn5;
				if (Math.abs(pn4) >= overflow) {
					pn0 /= overflow;
					pn1 /= overflow;
					pn2 /= overflow;
					pn3 /= overflow;
				}
			} while (true);
			gin = 1 - factor * gin;
		} else {
			// series expansion
			gin = 1;
			term = 1;
			rn = alpha;
			do {
				rn++;
				term *= x / rn;
				gin += term;
			} while (term > accurate);
			gin *= factor / alpha;
		}
		return gin;
	}

	private static double incompleteGammaP(double a, double x) {
		/*
		 * Incomplete Gamma function P(a,x) = 1-Q(a,x) (a cleanroom implementation of
		 * Numerical Recipes gammp(a,x); in Mathematica this function is
		 * 1-GammaRegularized)
		 */
		return incompleteGamma(x, a, lnGamma(a));
	}

	protected static double erf(double x) {
		/*
		 * Error Function: The erf() function can be used to compute traditional
		 * statistical functions such as the cumulative standard normal distribution
		 */
		if (x > 0.0) {
			return incompleteGammaP(0.5, x * x);
		} else if (x < 0.0) {
			return -incompleteGammaP(0.5, x * x);
		} else {
			return 0.0;
		}
	}

}
