import { CacheStatus } from "./cacheStatus";
import { Statistic } from "./statistic";

export class RequestStat {
    createdAt! : Date;
    country! : string;
    fetchDays! : number;
    startDate! : Date;
    cacheStatus! : CacheStatus;
    statistics! : Statistic[];

}