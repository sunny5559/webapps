package org.tfa.mtld.scoring;

import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;

/**
 * This interface specifies the methods required for any scoring criteria.
 */
public interface ScoringCriteriaStrategy {

	/**
	 * Score an MTLD against an individual corps member, to get the match score
	 * between the two for this criteria.
	 * 
	 * @param mtld
	 *            The MTLD you are calculating the score for.
	 * @param cm
	 *            The corps member being matched against.
	 * 
	 * @return A number between 0 and 1 indicating the quality of the match
	 *         between the two.
	 */
	public double scoreMtldToCorpsMember(MTLD mtld, CorpsMember cm);

	/**
	 * Score a corps member against an entire cohort, to get the match score for
	 * this criteria. If the specified corps member is part of the cohort, it
	 * will not be scored against itself.
	 * 
	 * @param cm
	 *            The corps member being scored.
	 * @param cohort
	 *            The cohort you are matching against.
	 * 
	 * @return A number between 0 and 1 indicating the quality of the match for
	 *         the corps member to the cohort
	 */
	public double scoreCorpsMemberToCohort(CorpsMember cm, Cohort cohort);

	/**
	 * Score an MTLD against a cohort, to get the match score between the two
	 * for this criteria.
	 * 
	 * @param mtld
	 *            The MTLD you are calculating the score for.
	 * @param cohort
	 *            The cohort you are matching against.
	 * 
	 * @return A number between 0 and 1 indicating the quality of the match
	 *         between the two.
	 */
	public double scoreMtldToCohort(MTLD mtld, Cohort cohort);
}
