package course.concurrency.exams.auction;

public class AuctionStoppablePessimistic implements AuctionStoppable {

    private Notifier notifier;

    public AuctionStoppablePessimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private final Object lock = new Object();

    private boolean isAuctionStopped = false;

    private volatile Bid latestBid = new Bid(-10000000L, -10000000L, -10000000L);

    public boolean propose(Bid bid) {
        if (!isAuctionStopped && (bid.getPrice() > latestBid.getPrice())) {
            synchronized (lock) {
                if (!isAuctionStopped && (bid.getPrice() > latestBid.getPrice())) {
                    notifier.sendOutdatedMessage(latestBid);
                    latestBid = bid;
                    return true;
                }
            }
        }
        return false;
    }

    public Bid getLatestBid() {
        return latestBid;
    }

    public synchronized Bid stopAuction() {
        isAuctionStopped = true;
        return latestBid;
    }
}
