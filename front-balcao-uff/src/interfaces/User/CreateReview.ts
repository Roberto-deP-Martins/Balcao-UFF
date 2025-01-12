export interface CreateReview {
  reviewerId: number;
  reviewedId: number;
  rating: number;
  comment: string;
}
