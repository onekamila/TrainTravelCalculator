using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using Oracle.ManagedDataAccess.Client;
using Dapper;

namespace train_tracker.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class TrainTrackController : ControllerBase
    {
        private readonly ILogger<TrainTrackController> _logger;

        public TrainTrackController(ILogger<TrainTrackController> logger)
        {
            _logger = logger;
        }

        [HttpPost]
        public IEnumerable<TrainInformation> Post([FromBody]TrainQuery query)
        {
            string departureDelay = null;
            string arrivalDelay = null;
			try
			{
                var conn = this.GetConnection();
                if (conn.State == ConnectionState.Closed)
                {
                    conn.Open();
                }

                if (conn.State == ConnectionState.Open)
                {
                    var query = "GET_HISTORICAL_ARRIVAL_DELAY";
                    departureDelay = SqlMapper.Query(conn, query, commandType: CommandType.StoredProcedure);

                    query = "GET_HISTORICAL_DEPARTURE_DELAY";
                    arrivalDelay = SqlMapper.Query(conn, query, commandType: CommandType.StoredProcedure);
                }
            }
			catch (Exception ex)
			{
				throw ex;
			}

            TrainInformation trainInfo = new()
            {
                TrainName = query.TrainId,
                HistoricalDepartureDelay = departureDelay,
                HistoricalArrivalDelay = arrivalDelay,
                ScheduledDeparture = query.ScheduledDeparture,
                ScheduledArrival = query.ScheduledArrival
            };

            return new List<TrainInformation>() { trainInfo };
        }
    }

    public IDbConnection GetConnection()
    {
        var connectionString = configuration.GetSection("ConnectionStrings").GetSection("TrainDataConnection").Value;
        var conn = new OracleConnection(connectionString);
        return conn;
    }
}
